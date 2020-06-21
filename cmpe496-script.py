from mitmproxy import http
from mitmproxy import ctx
import json

def beautifyHeaders(headers):
    beautified = "{"
    for header in headers:
        beautified += "'" + header + "'"
        beautified += ":"
        beautified += "'" + headers[header] + "'"
        beautified += ","

    if (beautified[-1] == ","):
        beautified = beautified[:-1]
    beautified += "}"

    return beautified


def duplicateAndRedirect(flow):

    # Avoid an infinite loop by not replaying already replayed requests
    if flow.request.is_replay:
        return

    oldHost = flow.request.host
    oldUrl = flow.request.url
    oldMethod = flow.request.method
    oldHeaders = beautifyHeaders(flow.request.headers)
    oldContent = flow.request.get_text()
    oldResponse = flow.response.get_text()
	 oldStatusCode = flow.response.status_code

    flow = flow.copy()

    # Only interactive tools have a view. If we have one, add a duplicate entry
    # for our flow.
    if "view" in ctx.master.addons:
        ctx.master.commands.call("view.flows.add", [flow])

    # set duplicated request's method and url so that it is routed to main server
    flow.request.method = "POST"
    flow.request.url = "http://main.apitesting.info/capture"

    # wrap original request and response's information that will be sent to main server
    wrappedFlow = {
        "content": oldContent,
        "method": oldMethod,
        "headers": oldHeaders,
        "response": oldResponse,
        "url": oldUrl,
        "hostname": oldHost,
		  "statusCode": oldStatusCode
    }

    flow.request.headers["Content-Type"] = "application/json"
    flow.request.content = str.encode(json.dumps(wrappedFlow))

    ctx.master.commands.call("replay.client", [flow])

def fuzz(flow) -> None:
    pass
    
def response(flow: http.HTTPFlow) -> None:
    # Duplicate all requests, requests that will be saved are filtered at the server
    duplicateAndRedirect(flow)

from mitmproxy import http
from mitmproxy import ctx

def duplicateAndRedirect(flow):
    # Avoid an infinite loop by not replaying already replayed requests
    if flow.request.is_replay:
        return

    oldPath = flow.request.path
    oldMethod = flow.request.method
    oldHeaders = flow.request.headers
    oldContent = flow.request.content

    flow = flow.copy()
    # Only interactive tools have a view. If we have one, add a duplicate entry
    # for our flow.
    if "view" in ctx.master.addons:
        ctx.master.commands.call("view.flows.add", [flow])

    flow.request.host = "apitesting-env.eba-bv6gapyk.eu-central-1.elasticbeanstalk.com"
    flow.request.path = "/capture"
    flow.request.query["content"] = oldContent
    flow.request.query["method"] = oldMethod
    flow.request.query["path"] = oldPath
    flow.request.query["headers"] = " ".join(oldHeaders)
    
    ctx.master.commands.call("replay.client", [flow])

def request(flow: http.HTTPFlow) -> None:
    print(flow.request)
    if flow.request.host == "demoapi-env.eba-qbx3xxbv.eu-central-1.elasticbeanstalk.com":
        duplicateAndRedirect(flow)
    

from mitmproxy import http
from mitmproxy import ctx

def duplicateAndRedirect(flow):

    # Avoid an infinite loop by not replaying already replayed requests
    if flow.request.is_replay:
        return

    print("Original Request: ", end="")
    print(flow.request.url)
    print("Original Response: ", end="")
    print(flow.response.content.decode("utf-8"))
    print()

    oldPath = flow.request.path
    oldMethod = flow.request.method
    oldHeaders = flow.request.headers
    oldContent = flow.request.content
    oldResponse = flow.response.content.decode("utf-8")

    flow = flow.copy()
    # Only interactive tools have a view. If we have one, add a duplicate entry
    # for our flow.
    if "view" in ctx.master.addons:
        ctx.master.commands.call("view.flows.add", [flow])

    flow.request.host = "main.apitesting.info"
    flow.request.path = "/capture"

    wrappedFlow = {
        "content": oldContent,
        "method": oldMethod,
        "path": oldPath,
        "headers": " ".join(oldHeaders),
        "response": oldResponse
    }

    flow.request.content = str.encode(str(wrappedFlow))
    
    print("Duplicated Request: ", end="")
    print(flow.request.content)
    print()

    ctx.master.commands.call("replay.client", [flow])

def fuzz(flow) -> None:
    pass
    
def response(flow: http.HTTPFlow) -> None:
    if flow.request.host == "demo.apitesting.info":
        duplicateAndRedirect(flow)


    duplicatedResponse = flow.response
    

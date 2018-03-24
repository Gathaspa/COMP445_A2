A simple HTTP file server implemented on socket channels. It works with most web browsers (run the main then go to http://localhost:8007/ . Note that only Firefox has been tested). It also offers a client application (change the function, in the main, from `runServer(args)` to `runClient(args)` and run the app. Make sure to have the server running first! Once you have the client running, you may write to it your HTTP request, and send it by writing `.` on a single line.

* Supports multiple clients reading and writing at the same time using synchronisation on files and socket channels.

* Supports file fetching with GET, and file creation or overwriting with POST.

* Can serve the file list in plain text, HTML, XML, or JSON

* Supports the direct download of files if your HTTP request for a file has the header `Content-Disposition: attachment`.




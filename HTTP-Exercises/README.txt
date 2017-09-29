# Exercise 1 #

- I see the HTTP requests from the server with info about the page and the server.

# Exercise 2 #

- The connection header implies whether or not the connection established to the client
should be closed or kept open for subsesquent requests. "keep-alive" describes a persistent
connection.

# Exercise 5 #

- 'get' method posts the parameters to the url, also hidden ones.
- 'post' method puts the parameters in the request under 'form data'.

# Exercise 6 #

- f) The server can maintain state between subsequent calls by saving data
to the session object of the client.

# Exercise 7 #
- f) Cookies can be used to store data on the clients haddrive and can be
set to be persistent so that the "state" can be resumed if the cookies are
still there when the page loads.
from flask import Flask, render_template

app = Flask(__name__)

@app.after_request
def add_csp(response):

    response.headers[
        "Content-Security-Policy"
    ] = "default-src 'self'; script-src 'self'"

    return response


@app.route("/")
def home():

    return render_template(
        "csp.html"
    )

if __name__ == "__main__":
    app.run(debug=True)
from flask import Flask, make_response

app = Flask(__name__)

@app.route("/secure")

def secure():

    response = make_response("""

    <h1>Secure Cookie</h1>

    <script>
    console.log(document.cookie);
    </script>

    """)

    response.set_cookie(
        "sessionid",
        "ABC123",
        httponly=True
    )

    return response


@app.route("/insecure")

def insecure():

    response = make_response("""

    <h1>Insecure Cookie</h1>

    <script>
    console.log(document.cookie);
    </script>

    """)

    response.set_cookie(
        "sessionid",
        "ABC123"
    )

    return response


if __name__ == "__main__":
    app.run(debug=True)
from flask import Flask, render_template, request, redirect
import sqlite3

app = Flask(__name__)

DATABASE = "database.db"

def init_db():

    conn = sqlite3.connect(DATABASE)

    conn.execute("""
    CREATE TABLE IF NOT EXISTS comments(
        id INTEGER PRIMARY KEY,
        text TEXT
    )
    """)

    conn.commit()
    conn.close()


@app.route("/", methods=["GET", "POST"])
def index():

    if request.method == "POST":

        comment = request.form["comment"]

        conn = sqlite3.connect(DATABASE)

        conn.execute(
            "INSERT INTO comments(text) VALUES(?)",
            (comment,)
        )

        conn.commit()
        conn.close()

        return redirect("/")

    conn = sqlite3.connect(DATABASE)

    comments = conn.execute(
        "SELECT text FROM comments"
    ).fetchall()

    conn.close()

    return render_template(
        "index.html",
        comments=comments
    )

if __name__ == "__main__":
    init_db()
    app.run(debug=True)
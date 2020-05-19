CD ./venv/Scripts
CALL activate
CD ..\..\

CMD /k "pip install -r requirements.txt & python run.py"

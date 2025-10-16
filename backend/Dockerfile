FROM python:3.12-slim

WORKDIR /app

RUN apt-get update && apt-get install -y \
    build-essential \
    libpq-dev \
    && rm -rf /var/lib/apt/lists/*

COPY requirements.txt .

RUN pip install --no-cache-dir -r requirements.txt


EXPOSE 4000
COPY . .

# Run Flask app
ENV FLASK_RUN_HOST=0.0.0.0
ENV FLASK_RUN_PORT=4000
CMD ["python", "app.py"]


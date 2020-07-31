# Job Service

This demonstrates a very simple job submission service. It can be tested using the following commands:

List jobs:

    curl --request GET \
      --url http://localhost:8080/jobs \
      --header 'content-type: application/json'

Create a job (`sleepMs` is optional):

    curl --request POST \
      --url 'http://localhost:8080/jobs?sleepMs=10000' \
      --header 'content-type: application/json' \
      --data '{}'

Get a job:

    curl --request GET \
      --url http://localhost:8080/jobs/1cdd0d5f-6c58-468d-8064-43e7d6a63919 \
      --header 'content-type: application/json'

Update a job via its status:

    curl --request PUT \
      --url http://localhost:8080/jobs/3b6a3961-7e9e-42a7-ba1d-7e02895fa74b \
      --header 'content-type: application/json' \
      --data '{
    	"status": "CANCELLED"
    }'

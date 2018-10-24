oc apply -f templates/

oc new-app --template=amq-broker-71-basic \
  -p APPLICATION_NAME=broker \
  -p IMAGE_STREAM_NAMESPACE=$(oc project -q) \
  -p AMQ_PROTOCOL=amqp \
  -p AMQ_QUEUES=example/strings \
  -p AMQ_USER=example \
  -p AMQ_PASSWORD=example

oc new-app --template=hello-world-jms-sender
oc new-app --template=hello-world-jms-receiver

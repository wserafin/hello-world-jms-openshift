# Getting started with Artemis on OpenShift

This guide shows how to send and receive messages using JMS and
ActiveMQ Artemis on OpenShift.  It uses the AMQP 1.0 message protocol
to send and receive messages.

## Prerequisites

* You must have access to an OpenShift instance and be logged in.
  [Minishift](https://docs.okd.io/latest/minishift/getting-started/index.html)
  provides a way to run OpenShift in your local environment.

* You must have selected a project in which the frontend and backend
  processes will be deployed.

## Deploying the services on OpenShift

1. If you haven't already, use `git` to clone example project source
   from GitHub.

        git clone https://github.com/amq-io/hello-world-jms-openshift

1. Change directory to the example project.  The subsequent commands
   assume it is your current directory.

        cd hello-world-jms-openshift/

1. Apply the project OpenShift templates.

        oc apply -f templates/

1. Deploy the broker service.

        oc new-app --template=amq-broker-71-basic \
          -p APPLICATION_NAME=broker \
          -p IMAGE_STREAM_NAMESPACE=$(oc project -q) \
          -p AMQ_PROTOCOL=amqp \
          -p AMQ_QUEUES=example/strings \
          -p AMQ_USER=example \
          -p AMQ_PASSWORD=example

1. Deploy the message sender service.

        oc new-app --template=hello-world-jms-sender

1. Deploy the message receiver service.

        oc new-app --template=hello-world-jms-receiver

1. Use your browser to check the status of your services.  You should
   see three applications, each with one pod.

## Testing the application

XXX

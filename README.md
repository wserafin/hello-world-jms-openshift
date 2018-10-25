# Getting started with Artemis on OpenShift

This guide shows how to send and receive messages using JMS and
ActiveMQ Artemis on OpenShift.  It uses the AMQP 1.0 message protocol
to send and receive messages.

## Overview

The example application has three parts.

* An AMQP 1.0 message broker, Artemis

* A sender service exposing an HTTP endpoint that converts HTTP
  requests into AMQP 1.0 messages.  It sends the messages to a queue
  called `example/strings` on the broker.

* A receiver service that exposes another HTTP endpoint, this time one
  that causes it to consume an AMQP message from `example/strings` and
  convert it back to an HTTP response.

The sender and the receiver use the JMS API perform their messaging
tasks.

## Prerequisites

* You must have access to an OpenShift instance and be logged in.
  [Minishift](https://docs.okd.io/latest/minishift/getting-started/index.html)
  provides a way to run OpenShift in your local environment.

* You must have a project selected in which to deploy the services.

## Deploying the services on OpenShift

1. If you haven't already, use `git` to clone the example source to
   your local environment.

        git clone https://github.com/amq-io/hello-world-jms-openshift

1. Change directory to the example project.  The subsequent commands
   assume it is your current directory.

        cd hello-world-jms-openshift/

1. Use the `oc apply` command to load the project templates.

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

## Exercising the application

The application exposes two HTTP endpoints, one for sending messages
and one for receiving them.

    http://<sender-host>/api/send
    http://<receiver-host>/api/receive

The `<sender-host>` and `<receiver-host>` values vary with each
deployment.  Use the web interface to find the precise values.  They
are listed on the right side of each service ("application") listed in
the overview.

To send a message, use the `curl` command.  The value you supply for
`string` is used as the message payload.

    curl -X POST --data "string=Hello!" http://<sender-host>/api/send

If things go as planned, it will return `OK`.  If things go awry, add
the `-v` flag to see more about what's happening.

To then receive the message back, use the `curl` command again against
the receiver.  If no message is available, it will print `null`.

    curl -X POST http://<receiver-host>/api/receive

Upon success, you should see the message you sent echoed back in the
response.  Here's some sample output from a few operations:

    $ curl -X POST --data "string=hello 1" http://sender-t2.6923.rh-us-east-1.openshiftapps.com/api/send
    OK
    $ curl -X POST --data "string=hello 2" http://sender-t2.6923.rh-us-east-1.openshiftapps.com/api/send
    OK
    $ curl -X POST http://receiver-t2.6923.rh-us-east-1.openshiftapps.com/api/receive
    hello 1
    $ curl -X POST http://receiver-t2.6923.rh-us-east-1.openshiftapps.com/api/receive
    hello 2 

## Exploring the example code

Here we take a closer look at how the code works.

XXX

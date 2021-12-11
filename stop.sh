#!/bin/bash
PID=$(cat /var/run/cfpartner_52465.pid)
kill -9 $PID
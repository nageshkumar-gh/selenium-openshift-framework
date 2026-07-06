#!/bin/sh
set -e

# Lets the Job's pod override thread count via an env var instead of baking it into the image,
# so tuning parallelism doesn't require a rebuild - only actual code/dependency changes do.
THREAD_COUNT="${THREAD_COUNT:-2}"
sed -i "s/data-provider-thread-count=\"[0-9]*\"/data-provider-thread-count=\"${THREAD_COUNT}\"/" testng.xml

exec mvn -B test -Dbrowser=chrome -Dheadless=true

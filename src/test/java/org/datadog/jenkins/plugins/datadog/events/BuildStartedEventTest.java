/*
The MIT License

Copyright (c) 2015-Present Datadog, Inc <opensource@datadoghq.com>
All rights reserved.

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
 */

package org.datadog.jenkins.plugins.datadog.events;

import hudson.EnvVars;
import hudson.model.*;
import jenkins.model.Jenkins;
import org.datadog.jenkins.plugins.datadog.DatadogEvent;
import org.datadog.jenkins.plugins.datadog.DatadogUtilities;
import org.datadog.jenkins.plugins.datadog.clients.DatadogClientStub;
import org.datadog.jenkins.plugins.datadog.model.BuildData;
import org.datadog.jenkins.plugins.datadog.stubs.BuildStub;
import org.datadog.jenkins.plugins.datadog.stubs.ProjectStub;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BuildStartedEventTest {

    @Test
    public void testWithNothingSet() throws IOException, InterruptedException {
        Jenkins jenkins = mock(Jenkins.class);
        when(jenkins.getFullName()).thenReturn(null);

        ProjectStub job = new ProjectStub(jenkins,null);

        Run run = new BuildStub(job, null, null, null, 0L, 0, null, 0L, null);

        TaskListener listener = mock(TaskListener.class);
        BuildData bd = new BuildData(run, listener);
        DatadogEvent event = new BuildStartedEventImpl(bd);

        String hostname = DatadogUtilities.getHostname(null);
        Assert.assertTrue(event.getHost().equals(hostname));
        Assert.assertTrue(event.getDate() != 0);
        Assert.assertTrue(event.getAggregationKey().equals("unknown"));
        Assert.assertTrue(event.getTags().size() == 1);
        Assert.assertTrue(event.getTags().get("job").contains("unknown"));
        Assert.assertTrue(event.getTitle().equals("Job unknown build #0 started on " + hostname));
        Assert.assertTrue(event.getText().contains("User anonymous started the [job unknown build #0](unknown) (0.00 secs)"));
        Assert.assertTrue(event.getAlertType().equals(DatadogEvent.AlertType.INFO));
        Assert.assertTrue(event.getPriority().equals(DatadogEvent.Priority.LOW));
    }

    @Test
    public void testWithNothingSet_parentFullName() throws IOException, InterruptedException {
        Jenkins jenkins = mock(Jenkins.class);
        when(jenkins.getFullName()).thenReturn("parentFullName");

        ProjectStub job = new ProjectStub(jenkins,null);

        Run run = new BuildStub(job, null, null, null, 0L, 0, null, 0L, null);

        TaskListener listener = mock(TaskListener.class);
        BuildData bd = new BuildData(run, listener);
        DatadogEvent event = new BuildStartedEventImpl(bd);

        String hostname = DatadogUtilities.getHostname(null);
        Assert.assertTrue(event.getHost().equals(hostname));
        Assert.assertTrue(event.getDate() != 0);
        Assert.assertTrue(event.getAggregationKey().equals("parentFullName/null"));
        Assert.assertTrue(event.getTags().size() == 1);
        Assert.assertTrue(event.getTags().get("job").contains("parentFullName/null"));
        Assert.assertTrue(event.getTitle().equals("Job parentFullName/null build #0 started on " + hostname));
        Assert.assertTrue(event.getText().contains("User anonymous started the [job parentFullName/null build #0](unknown) (0.00 secs)"));
        Assert.assertTrue(event.getAlertType().equals(DatadogEvent.AlertType.INFO));
        Assert.assertTrue(event.getPriority().equals(DatadogEvent.Priority.LOW));
    }

    @Test
    public void testWithNothingSet_parentFullName_2() throws IOException, InterruptedException {
        Jenkins jenkins = mock(Jenkins.class);
        when(jenkins.getFullName()).thenReturn("parent»Full  Name");

        ProjectStub job = new ProjectStub(jenkins,null);

        Run run = new BuildStub(job, null, null, null, 0L, 0, null, 0L, null);

        TaskListener listener = mock(TaskListener.class);
        BuildData bd = new BuildData(run, listener);
        DatadogEvent event = new BuildStartedEventImpl(bd);

        String hostname = DatadogUtilities.getHostname(null);
        Assert.assertTrue(event.getHost().equals(hostname));
        Assert.assertTrue(event.getDate() != 0);
        Assert.assertTrue(event.getAggregationKey().equals("parent/FullName/null"));
        Assert.assertTrue(event.getTags().size() == 1);
        Assert.assertTrue(event.getTags().get("job").contains("parent/FullName/null"));
        Assert.assertTrue(event.getTitle().equals("Job parent/FullName/null build #0 started on " + hostname));
        Assert.assertTrue(event.getText().contains("User anonymous started the [job parent/FullName/null build #0](unknown) (0.00 secs)"));
        Assert.assertTrue(event.getAlertType().equals(DatadogEvent.AlertType.INFO));
        Assert.assertTrue(event.getPriority().equals(DatadogEvent.Priority.LOW));
    }

    @Test
    public void testWithNothingSet_jobName() throws IOException, InterruptedException {
        Jenkins jenkins = mock(Jenkins.class);
        when(jenkins.getFullName()).thenReturn("parentFullName");

        ProjectStub job = new ProjectStub(jenkins,"jobName");

        Run run = new BuildStub(job, null, null, null, 0L, 0, null, 0L, null);

        TaskListener listener = mock(TaskListener.class);
        BuildData bd = new BuildData(run, listener);
        DatadogEvent event = new BuildStartedEventImpl(bd);

        String hostname = DatadogUtilities.getHostname(null);
        Assert.assertTrue(event.getHost().equals(hostname));
        Assert.assertTrue(event.getDate() != 0);
        Assert.assertTrue(event.getAggregationKey().equals("parentFullName/jobName"));
        Assert.assertTrue(event.getTags().size() == 1);
        Assert.assertTrue(event.getTags().get("job").contains("parentFullName/jobName"));
        Assert.assertTrue(event.getTitle().equals("Job parentFullName/jobName build #0 started on " + hostname));
        Assert.assertTrue(event.getText().contains("User anonymous started the [job parentFullName/jobName build #0](unknown) (0.00 secs)"));
        Assert.assertTrue(event.getAlertType().equals(DatadogEvent.AlertType.INFO));
        Assert.assertTrue(event.getPriority().equals(DatadogEvent.Priority.LOW));
    }

    @Test
    public void testWithNothingSet_result() throws IOException, InterruptedException {
        Jenkins jenkins = mock(Jenkins.class);
        when(jenkins.getFullName()).thenReturn("parentFullName");

        ProjectStub job = new ProjectStub(jenkins,"jobName");

        Run run = new BuildStub(job, Result.FAILURE, null, null, 0L, 0, null, 0L, null);

        TaskListener listener = mock(TaskListener.class);
        BuildData bd = new BuildData(run, listener);
        DatadogEvent event = new BuildStartedEventImpl(bd);

        String hostname = DatadogUtilities.getHostname(null);
        Assert.assertTrue(event.getHost().equals(hostname));
        Assert.assertTrue(event.getDate() != 0);
        Assert.assertTrue(event.getAggregationKey().equals("parentFullName/jobName"));
        Assert.assertTrue(event.getTags().size() == 2);
        Assert.assertTrue(event.getTags().get("job").contains("parentFullName/jobName"));
        Assert.assertTrue(event.getTags().get("result").contains("FAILURE"));
        Assert.assertTrue(event.getTitle().equals("Job parentFullName/jobName build #0 started on " + hostname));
        Assert.assertTrue(event.getText().contains("User anonymous started the [job parentFullName/jobName build #0](unknown) (0.00 secs)"));
        Assert.assertTrue(event.getAlertType().equals(DatadogEvent.AlertType.INFO));
        Assert.assertTrue(event.getPriority().equals(DatadogEvent.Priority.LOW));
    }

    @Test
    public void testWithEverythingSet() throws IOException, InterruptedException {
        Jenkins jenkins = mock(Jenkins.class);
        when(jenkins.getFullName()).thenReturn("ParentFullName");

        ProjectStub job = new ProjectStub(jenkins,"JobName");

        EnvVars envVars = new EnvVars();
        envVars.put("HOSTNAME", "test-hostname-2");
        envVars.put("NODE_NAME", "test-node");
        envVars.put("BUILD_URL", "http://build_url.com");
        envVars.put("GIT_BRANCH", "test-branch");

        Run run = new BuildStub(job, null, envVars, null, 10L, 2, null, 0L, null);

        TaskListener listener = mock(TaskListener.class);

        BuildData bd = new BuildData(run, listener);
        DatadogEvent event = new BuildStartedEventImpl(bd);

        Assert.assertTrue(event.getHost().equals("test-hostname-2"));
        Assert.assertTrue(event.getDate() != 0);
        Assert.assertTrue(event.getAggregationKey().equals("ParentFullName/JobName"));
        Assert.assertTrue(event.getTags().size() == 3);
        Assert.assertTrue(event.getTags().get("job").contains("ParentFullName/JobName"));
        Assert.assertTrue(event.getTags().get("node").contains("test-node"));
        Assert.assertTrue(event.getTags().get("branch").contains("test-branch"));
        Assert.assertTrue(event.getTitle().equals("Job ParentFullName/JobName build #2 started on test-hostname-2"));
        Assert.assertTrue(event.getText().contains("User anonymous started the [job ParentFullName/JobName build #2](http://build_url.com) (0.01 secs)"));
        Assert.assertTrue(event.getAlertType().equals(DatadogEvent.AlertType.INFO));
        Assert.assertTrue(event.getPriority().equals(DatadogEvent.Priority.LOW));
    }

    @Test
    public void testWithEverythingSet_envVarsAndTags() throws IOException, InterruptedException {
        Jenkins jenkins = mock(Jenkins.class);
        when(jenkins.getFullName()).thenReturn("ParentFullName");

        ProjectStub job = new ProjectStub(jenkins,"JobName");

        EnvVars envVars = new EnvVars();
        envVars.put("BUILD_URL", "http://build_url.com");
        envVars.put("CVS_BRANCH", "csv-branch");
        envVars.put("SVN_BRANCH", "svn-branch");

        Run run = new BuildStub(job, null, envVars, null, 10L, 2, null, 0L, null);

        TaskListener listener = mock(TaskListener.class);

        BuildData bd = new BuildData(run, listener);
        Map<String, Set<String>> tags = new HashMap<>();
        tags = DatadogClientStub.addTagToMap(tags, "tag1", "value1");
        tags = DatadogClientStub.addTagToMap(tags, "tag2", "value2");
        bd.setTags(tags);
        DatadogEvent event = new BuildStartedEventImpl(bd);

        String hostname = DatadogUtilities.getHostname(null);
        Assert.assertTrue(event.getHost().equals(hostname));
        Assert.assertTrue(event.getDate() != 0);
        Assert.assertTrue(event.getAggregationKey().equals("ParentFullName/JobName"));
        Assert.assertTrue(event.getTags().size() == 4);
        Assert.assertTrue(event.getTags().get("job").contains("ParentFullName/JobName"));
        Assert.assertTrue(event.getTags().get("tag1").contains("value1"));
        Assert.assertTrue(event.getTags().get("tag2").contains("value2"));
        Assert.assertTrue(event.getTags().get("branch").contains("csv-branch"));
        Assert.assertTrue(event.getTitle().equals("Job ParentFullName/JobName build #2 started on " + hostname));
        Assert.assertTrue(event.getText().contains("User anonymous started the [job ParentFullName/JobName build #2](http://build_url.com) (0.01 secs)"));
        Assert.assertTrue(event.getAlertType().equals(DatadogEvent.AlertType.INFO));
        Assert.assertTrue(event.getPriority().equals(DatadogEvent.Priority.LOW));
    }
}

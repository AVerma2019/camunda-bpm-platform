/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.camunda.bpm.client.impl;

import org.camunda.bpm.client.ExternalTaskClient;
import org.camunda.bpm.client.interceptor.ClientRequestInterceptor;
import org.camunda.bpm.client.interceptor.impl.RequestInterceptorHandler;
import org.camunda.bpm.client.topic.TopicSubscriptionBuilder;
import org.camunda.bpm.client.topic.impl.TopicSubscriptionBuilderImpl;
import org.camunda.bpm.client.topic.impl.TopicSubscriptionManager;

import java.util.List;

/**
 * @author Tassilo Weidner
 */
public class ExternalTaskClientImpl implements ExternalTaskClient {

  private TopicSubscriptionManager topicSubscriptionManager;
  private RequestInterceptorHandler requestInterceptorHandler;

  public ExternalTaskClientImpl(ExternalTaskClientBuilderImpl clientBuilder) {
    String workerId = clientBuilder.getWorkerId();
    String endpointUrl = clientBuilder.getEndpointUrl();

    List<ClientRequestInterceptor> interceptors = clientBuilder.getInterceptors();
    requestInterceptorHandler = new RequestInterceptorHandler(interceptors);

    EngineClient engineClient = new EngineClient(workerId, endpointUrl, requestInterceptorHandler);
    topicSubscriptionManager = new TopicSubscriptionManager(engineClient);
  }

  public TopicSubscriptionBuilder subscribe(String topicName) {
    return new TopicSubscriptionBuilderImpl(topicName, topicSubscriptionManager);
  }

  public void shutdown() {
    topicSubscriptionManager.shutdown();
  }

  public TopicSubscriptionManager getWorkerManager() {
    return topicSubscriptionManager;
  }

  public RequestInterceptorHandler getRequestInterceptorHandler() {
    return requestInterceptorHandler;
  }

}

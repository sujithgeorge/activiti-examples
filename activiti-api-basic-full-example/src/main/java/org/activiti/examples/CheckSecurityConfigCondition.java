package org.activiti.examples;

import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.ConfigurationCondition;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class CheckSecurityConfigCondition implements ConfigurationCondition {
 
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        return !context.getBeanFactory().containsBeanDefinition("WebSecurityConfig");
    }
 
    public ConfigurationPhase getConfigurationPhase() {
        return ConfigurationPhase.REGISTER_BEAN;
    }
}

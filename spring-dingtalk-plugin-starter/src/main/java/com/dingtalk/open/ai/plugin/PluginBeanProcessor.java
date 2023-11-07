package com.dingtalk.open.ai.plugin;

import com.dingtalk.open.ai.plugin.annotation.AIPlugin;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Method;

/**
 * @author feiyin
 * @date 2023/11/7
 */
public class PluginBeanProcessor implements BeanFactoryPostProcessor {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        for (String beanName : beanFactory.getBeanDefinitionNames()) {
            try {
                BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanName);
                if (beanDefinition.getBeanClassName() == null) {
                    continue;
                }
                Class beanClass = ClassUtils.forName(beanDefinition.getBeanClassName(), beanFactory.getBeanClassLoader());
                if (!beanClass.isAnnotationPresent(AIPlugin.class)) {
                    continue;
                }
                BeanDefinition definition = BeanDefinitionBuilder.genericBeanDefinition(AIPluginReporter.class)
                        .addPropertyReference("container", "pluginContainer")
                        .setInitMethodName("register")
                        .addPropertyValue("targetClass", beanClass)
                        .getBeanDefinition();

                ((DefaultListableBeanFactory)beanFactory).registerBeanDefinition("reporter$" + beanName, definition);

                for (Method method : beanClass.getMethods()) {
                    if (method.isAnnotationPresent(Graph.class)) {
                        BeanDefinition methodBean = BeanDefinitionBuilder.genericBeanDefinition(GraphMethodDescriptor.class)
                                .addPropertyValue("method", method)
                                .addPropertyReference("target", beanName)
                                .addPropertyReference("dispatcher","graphDispatcher")
                                .setInitMethodName("register")
                                .getBeanDefinition();
                        ((DefaultListableBeanFactory) beanFactory).registerBeanDefinition("reporter$" + beanName + method.getName(), methodBean);
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}

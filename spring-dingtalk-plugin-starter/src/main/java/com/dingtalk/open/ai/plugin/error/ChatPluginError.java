package com.dingtalk.open.ai.plugin.error;

/**
 * @author feiyin
 * @date 2023/11/13
 */
public enum ChatPluginError {


    READ_EXAMPLE_FILES_ERROR,


    ILLEGAL_EXAMPLE_CONTENT_FORMAT_ERROR,

    /**
     * 没有graph的注解
     */
    GRAPH_ANNOTATION_NOT_PRESENT_ERROR,
}

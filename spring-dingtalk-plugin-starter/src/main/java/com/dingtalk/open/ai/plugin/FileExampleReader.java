package com.dingtalk.open.ai.plugin;

import com.alibaba.fastjson.JSON;
import com.dingtalk.open.ai.plugin.error.ChatPluginError;
import com.dingtalk.open.ai.plugin.error.ChatPluginException;
import com.dingtalk.open.ai.plugin.schema.Example;
import com.dingtalk.open.app.api.util.IoUtils;
import com.dingtalk.open.app.stream.network.api.logger.InternalLogger;
import com.dingtalk.open.app.stream.network.api.logger.InternalLoggerFactory;

/**
 * @author feiyin
 * @date 2023/11/10
 */
public class FileExampleReader implements ExampleReader {

    private static final InternalLogger LOGGER = InternalLoggerFactory.getLogger(FileExampleReader.class);

    private final String path;

    public FileExampleReader(String path) {
        this.path = path;
    }

    @Override
    public Example[] read() throws ChatPluginException {

        byte[] content;
        try {
            content = IoUtils.readAll(Thread.currentThread().getContextClassLoader().getResourceAsStream(path));
        } catch (Exception e) {
            LOGGER.error("[DingTalk] failed to read examples from file, path={}", path, e);
            throw new ChatPluginException(ChatPluginError.READ_EXAMPLE_FILES_ERROR);
        }

        if (content == null) {
            return new Example[]{};
        }

        try {
            return JSON.parseObject(content, Example[].class);
        } catch (Exception e) {
            LOGGER.error("[DingTalk] illegal example file content format, path={}", path, e);
            throw new ChatPluginException(ChatPluginError.ILLEGAL_EXAMPLE_CONTENT_FORMAT_ERROR);
        }
    }

}

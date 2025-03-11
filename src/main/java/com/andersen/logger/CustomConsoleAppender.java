package com.andersen.logger;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.Layout;


public class CustomConsoleAppender extends ConsoleAppender<ILoggingEvent> {
    private Layout<ILoggingEvent> layout;

    @Override
    public void append(ILoggingEvent event) {
        // Optionally, you can format the log message
        String formattedMessage = layout.doLayout(event);
        super.append(event);
        // Here you can add any additional custom behavior
    }

    public void setLayout(Layout<ILoggingEvent> layout) {
        this.layout = layout;
    }
}
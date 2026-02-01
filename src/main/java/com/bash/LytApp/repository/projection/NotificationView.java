package com.bash.LytApp.repository.projection;

import java.time.LocalDateTime;

public interface NotificationView {
    String getType();
    String getMessage();
    LocalDateTime getSentAt();
    Boolean getIsRead();
}

package com.bash.LytApp.utility;

public record ResponseDto<T>(
        String message,
        boolean success,
        T data

) {
}

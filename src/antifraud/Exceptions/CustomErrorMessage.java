package antifraud.Exceptions;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CustomErrorMessage {
    private final int status;
    private final LocalDateTime timeStamp = LocalDateTime.now();
    private final String message;
}

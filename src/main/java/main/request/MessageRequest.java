package main.request;

import lombok.Data;

@Data
public class MessageRequest {

    private String text;
    private String name;
    private String phone;
}

package tk.finedesk.finedesk.enums;

import lombok.Getter;
import tk.finedesk.finedesk.utils.ConstantMessages;

@Getter
public enum ResponseEnum {

    RESPONSE_200_USER_REGISTRATION(ConstantMessages.SUCCESSFUL_USER_REGISTERED);

    private String message;

    ResponseEnum(String message) {
        this.message = message;
    }

}

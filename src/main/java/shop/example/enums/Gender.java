package shop.example.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import shop.example.exceptions.GenderInvalidException;

public enum Gender {
    MALE,
    FEMALE;

    @JsonCreator
    public static Gender fromString(String value){
        for(Gender gender: Gender.values()){
            if(gender.name().equalsIgnoreCase(value)){
                return gender;
            }
        }
        throw new GenderInvalidException(ErrorCode.GENDER_INVALID.getMessage());
    }
}

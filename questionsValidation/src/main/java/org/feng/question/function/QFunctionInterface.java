package org.feng.question.function;

import lombok.Getter;
import lombok.Setter;
import org.feng.question.function.finterface.StringConverter;

@Setter
@Getter
public class QFunctionInterface {
    private String name;

    public static void main(String[] args) {
        QFunctionInterface obj = new QFunctionInterface();
        obj.setName("aegon");

        StringConverter toUpperCaseConvert = String::toUpperCase;

        System.out.println(obj.getFormatName(toUpperCaseConvert));
    }

    public String getFormatName(StringConverter pre) {
        return pre.convert(this.getName());
    }

}

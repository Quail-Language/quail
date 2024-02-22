package me.tapeline.quailj.runtime.std.data.bytes;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.RuntimeStriker;
import me.tapeline.quailj.typing.classes.QObject;
import org.apache.commons.lang3.StringUtils;

public class BytesUtils {

    public static byte[] getByteArray(Runtime runtime, QObject object) throws RuntimeStriker {
        if (object instanceof DataLibBytes) {
            return DataLibBytes.validate(runtime, object).data;
        } else if (object.isStr()) {
            String s = object.strValue();
            s = StringUtils.repeat('0', s.length() % 8) + s;
            byte[] data = new byte[s.length() / 8];
            for (int i = 0; i < s.length() / 8; i++) {
                data[i] = Byte.parseByte(s.substring(i, (i + 1) * 8));
            }
            return data;
        } else return null;
    }

}
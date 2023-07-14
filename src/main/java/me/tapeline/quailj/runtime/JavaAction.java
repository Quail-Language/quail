package me.tapeline.quailj.runtime;

import me.tapeline.quailj.typing.classes.QObject;

public interface JavaAction {

    QObject action(Runtime runtime, Memory memory) throws RuntimeStriker;

}

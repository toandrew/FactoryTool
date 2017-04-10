package midicore;

/*********************************************
 * @简介: C++ 中编译此类对应的头文件用于
 * 实现自定义的处理数据处理
 * @作者: 张宇飞(zhangyufei@xiaoyezi.com)
 * @日期: 2016-11-23
 *********************************************/

public class NativeMidiDeviceEventListener implements MidiDeviceEventListener {
    native public void onAttached();
    native public void onDetached();
    native public void onMidiData(final byte d[]);
}

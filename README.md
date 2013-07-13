wiimote-virtual-joystick
========================

Scans for wiimotes and adds them as virtual joysticks on Linux

A project I made a while ago so that I could easily connect Wiimotes to my HTPC
for emulators and control.  Automatically connects up to four wiimotes, and they
appear as joysticks on Linux.  Code is provided here without much commentary.
You can find some instructions at http://forum.xbmc.org/showthread.php?tid=98440

Java was chosen because there were some really nice Java classes for interacting
with Wiimotes.  However, JNI was needed for uinput support.

If I'm bored one day, maybe I'll attempt to change this to pure C++.
#include <stdio.h>
#include <string.h>
#include <unistd.h>

#include "suinput.h"
#include "UInput.h"

/* Globals */
static int uinp_fd_wms[] = {-1, -1, -1, -1};
struct uinput_user_dev wms[4];
static int abs[] = {ABS_HAT0X, ABS_HAT0Y, ABS_X, ABS_Y};
static int btns[] = {BTN_A, BTN_B, BTN_SELECT, BTN_MODE, BTN_START, BTN_X, BTN_Y};

JNIEXPORT void JNICALL Java_UInput_destroy (JNIEnv *env, jobject obj) {
    printf("Java_UInput_destroy\n");
    int i;
    for(i=0;i<4;i++) {
        suinput_destroy(uinp_fd_wms[i]);
    }
    return;
}

JNIEXPORT void JNICALL Java_UInput_init (JNIEnv *env, jobject obj) {
    printf("Java_UInput_init\n");
    
    // Initialize four wiimotes
    int i;
    for(i=0;i<4;i++) {
        char name[26];
        sprintf(name, "Wiimote %d Virtual Joystick", i+1);
        memset(&wms[i], 0, sizeof(wms[i]));
        strcpy(wms[i].name, name);
        wms[i].id.bustype = BUS_VIRTUAL;
        wms[i].id.vendor = 0x1781;
        wms[i].id.product = 0x0a9d;
        wms[i].id.version = 0x101;
        
        uinp_fd_wms[i] = suinput_open();
        if(uinp_fd_wms[i] == -1) { printf("Error in opening device %d", i); }
        
        suinput_set_capabilities(uinp_fd_wms[i], EV_KEY, btns, 7);
        suinput_set_capabilities(uinp_fd_wms[i], EV_ABS, abs, 4);
        
        int j;
        for(j=0;j<4;j++) {
            wms[i].absmin[abs[j]] = 0;
            wms[i].absmax[abs[j]] = 1023;
        }
        
        for(j=0;j<4;j++) {
            wms[i].absfuzz[abs[j]] = 0;
            wms[i].absflat[abs[j]] = 0;
        }
        
        suinput_create(uinp_fd_wms[i], &wms[i]);
        
        sleep(1);
        
        for(j=0;j<4;j++) {
            suinput_write(uinp_fd_wms[i], EV_ABS, abs[j], 0);
        }
    }
    return;
}

JNIEXPORT void JNICALL Java_UInput_sendButtonDown (JNIEnv *env, jobject obj, jint wiimote, jint key) {
    printf("Java_UInput_sendButtonDown\n");
    // Down 7, left 8, right 9, up 10
    if(key == 7) {
        suinput_write(uinp_fd_wms[wiimote-1], EV_ABS, abs[1], 1);
    } else if(key == 10) {
        suinput_write(uinp_fd_wms[wiimote-1], EV_ABS, abs[1], -1);
    } else if(key == 8) {
        suinput_write(uinp_fd_wms[wiimote-1], EV_ABS, abs[0], -1);
    } else if(key == 9) {
        suinput_write(uinp_fd_wms[wiimote-1], EV_ABS, abs[0], 1);
    } else {
        suinput_write(uinp_fd_wms[wiimote-1], EV_KEY, btns[key], 1);
    }
    suinput_syn(uinp_fd_wms[wiimote-1]);
    return;
}

JNIEXPORT void JNICALL Java_UInput_sendButtonUp (JNIEnv *env, jobject obj, jint wiimote, jint key) {
    printf("Java_UInput_sendButtonUp\n");
    if(key == 7) {
        suinput_write(uinp_fd_wms[wiimote-1], EV_ABS, abs[1], 0);
    } else if(key == 10) {
        suinput_write(uinp_fd_wms[wiimote-1], EV_ABS, abs[1], 0);
    } else if(key == 8) {
        suinput_write(uinp_fd_wms[wiimote-1], EV_ABS, abs[0], 0);
    } else if(key == 9) {
        suinput_write(uinp_fd_wms[wiimote-1], EV_ABS, abs[0], 0);
    } else {
        suinput_write(uinp_fd_wms[wiimote-1], EV_KEY, btns[key], 0);
    }
    suinput_syn(uinp_fd_wms[wiimote-1]);
    return;
}

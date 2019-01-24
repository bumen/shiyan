import sys
import ctypes
from sdl2 import *


class audio_ctx:  # Context

    def __init__(self, fid, flag):
        self.f = open(fid, 'rb+')
        self.runflag = flag

    def __del__(self):
        self.f.close


def audio_cb(udata, stream, len):
    c = ctypes.cast(udata, ctypes.py_object).value
    buf = c.f.read(4096)
    if not buf:
        SDL_PauseAudio(1)
        c.runflag = 0
        return
    SDL_memset(stream, 0, len)
    SDL_MixAudio(
        stream, ctypes.cast(
            buf, POINTER(ctypes.c_ubyte)), len, SDL_MIX_MAXVOLUME)


def main():
    print ("begin ...")
    SDL_Init(SDL_INIT_AUDIO)
    ctx = audio_ctx('A_18.pcm', 1)
    audiocallback = audio.SDL_AudioCallback(audio_cb)
    reqspec = audio.SDL_AudioSpec(
        16000, audio.AUDIO_S16SYS, 1, 2048, audiocallback, id(ctx))
    spec = audio.SDL_AudioSpec(0, 0, 0, 0)  # nonsence
    audio.SDL_OpenAudio(reqspec, None)
    SDL_PauseAudio(0)
    while ctx.runflag:
        SDL_Delay(1)
    SDL_Quit()
    print ("exit ...")
    return 0


if __name__ == "__main__":
    sys.exit(main())

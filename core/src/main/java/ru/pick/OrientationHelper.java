package ru.pick;

public class OrientationHelper {
    private static OrientationListener listener;

    public static void setListener(OrientationListener listener) {
        OrientationHelper.listener = listener;
    }

    public static void lockCurrentOrientation() {
        if (listener != null) listener.lockCurrent();
    }

    public static void unlock() {
        if (listener != null) listener.unlock();
    }

    public interface OrientationListener {
        void lockCurrent();
        void unlock();
    }

        public enum ScreenOrientation {
            PORTRAIT,
            LANDSCAPE,
            UNDEFINED
        }

        private static OrientationProvider provider;

        public static void setProvider(OrientationProvider provider) {
            OrientationHelper.provider = provider;
        }

        public static ScreenOrientation getOrientation() {
            return provider != null ? provider.getOrientation() : ScreenOrientation.UNDEFINED;
        }

        public interface OrientationProvider {
            ScreenOrientation getOrientation();
        }

}

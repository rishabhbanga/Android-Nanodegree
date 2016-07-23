package rishabhbanga.nanodegree.tnimdb.bus;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

/**
 * Created by erishba on 7/22/2016.
 */
public class EventBus
{
        private static Bus event;

        private static Bus getBus() {
            if (event == null) {
                event = new Bus(ThreadEnforcer.MAIN);
            }
            return event;
        }

        //Wrapper method to register event
        public static void register(Object event) {
            getBus().register(event);
        }

        //Wrapper method to unregister event
        public static void unregister(Object event) {
            getBus().unregister(event);
        }

        //Wrapper method to post event
        public static void post(Object event) {
            getBus().post(event);
        }
}

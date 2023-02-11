package uk.pixtle.application.events;

import lombok.Getter;
import lombok.Setter;
import uk.pixtle.application.Application;
import uk.pixtle.application.ApplicationComponent;
import uk.pixtle.application.events.annotations.EventHandler;
import uk.pixtle.application.events.events.Event;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

public class EventManager extends ApplicationComponent {

    record ClassMethodEntry(
            Object clazz,
            Method method)
    {}

    @Getter
    @Setter
    Application application;

    @Getter
    private HashMap<Type, ArrayList<ClassMethodEntry>> methodsByEventType = new HashMap<>();

    // Registry

    private void registerEntry(Type paramType, Object paramClass, Method paramMethod) {

        ClassMethodEntry entry = new ClassMethodEntry(paramClass, paramMethod);

        if (this.getMethodsByEventType().containsKey(paramType)) {
            this.getMethodsByEventType().get(paramType).add(entry);
            return;
        }

        ArrayList<ClassMethodEntry> entries = new ArrayList<>();
        entries.add(entry);

        this.getMethodsByEventType().put(paramType, entries);
    }

    public void registerEvents(Object paramObject) {

        for(Method method : paramObject.getClass().getDeclaredMethods()) {
            for(Annotation annotation : method.getDeclaredAnnotationsByType(EventHandler.class)) {

                if(!method.getReturnType().equals(Void.TYPE) || method.getParameterCount() != 1) {
                    // TO-DO exception
                    continue;
                }

                Type type = method.getParameterTypes()[0];

                this.registerEntry(type, paramObject, method);
            }
        }
    }

    // Calling

    private void callEvent(ClassMethodEntry paramEntry, Record paramRecord) {
        try {
            paramEntry.method().invoke(paramEntry.clazz(), paramRecord);
        } catch (IllegalAccessException | InvocationTargetException exception) {
            // TO-DO
            return;
        }
    }

    public void callEvent(Record paramRecord) {

        if(!(paramRecord instanceof Event)) {
            // TO-DO exception
            return;
        }

        Type type = paramRecord.getClass();

        if(!this.getMethodsByEventType().containsKey(type)) {
            return;
        }

        for(ClassMethodEntry entry : this.getMethodsByEventType().get(type)) {
            this.callEvent(entry, paramRecord);
        }
    }


    public EventManager(Application paramApplication) {
        super(paramApplication);

        this.setApplication(paramApplication);
    }
}

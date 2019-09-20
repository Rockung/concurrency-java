package org.example.juc;

import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.atomic.AtomicBoolean;

public class AtomicBooleanTest {
    @Test
    public void testCreateWithoutArguments() {
        AtomicBoolean bool = new AtomicBoolean();
        assertFalse(bool.get());
    }

    @Test
    public void testCreateWithArguments() {
        AtomicBoolean bool = new AtomicBoolean(true);
        assertTrue(bool.get());
    }

    @Test
    public void testGetAndSet() {
        AtomicBoolean bool = new AtomicBoolean(true);
        boolean result = bool.getAndSet(false);
        assertTrue(result);
        assertFalse(bool.get());
    }

    @Test
    public void testGetAndSetSame() {
        AtomicBoolean bool = new AtomicBoolean(true);
        boolean result = bool.getAndSet(true);
        assertTrue(result);
        assertTrue(bool.get());
    }

    @Test
    public void testCompareAndSet() {
        AtomicBoolean bool = new AtomicBoolean(true);
        boolean result = bool.compareAndSet(true, false);
        assertTrue(result);
        assertFalse(bool.get());
    }

    @Test
    public void testCompareAndSetFailed() {
        AtomicBoolean bool = new AtomicBoolean(true);
        boolean result = bool.compareAndSet(false, true);
        assertFalse(result);
        assertTrue(bool.get());
    }

}

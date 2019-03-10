/*
 * Regnum - A Discord bot clustering system made for Hawk
 *
 * Copyright (C) 2019  Michael Rittmeister
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see https://www.gnu.org/licenses/.
 */

package cc.hawkbot.regnum.waiter.impl;

import kotlin.jvm.JvmClassMappingKt;
import kotlin.reflect.KClass;
import net.dv8tion.jda.api.events.GenericEvent;
import org.jetbrains.annotations.NotNull;

import java.io.Closeable;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

@SuppressWarnings("unused")
public interface EventWaiter extends Closeable {

    <T extends @NotNull GenericEvent> CompletionStage<T> waitFor(@NotNull Class<T> event, @NotNull Predicate<T> predicate,
    long timeout, @NotNull TimeUnit timeoutUnit);

    default <T extends @NotNull GenericEvent> CompletionStage<T> waitFor(@NotNull KClass<T> event, @NotNull Predicate<T> predicate,
                                                                 long timeout, @NotNull TimeUnit timeoutUnit) {
        return waitFor(JvmClassMappingKt.getJavaClass(event), predicate, timeout, timeoutUnit);
    }

    class TimeoutException extends RuntimeException {
        public TimeoutException(String message) {
            super(message);
        }
    }

}

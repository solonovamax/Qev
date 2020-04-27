/*
 * Copyright (c) 2020 solonovamax <solonovamax@12oclockpoint.com>
 *
 *       This program is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU General Public License as published by
 *       the Free Software Foundation, either version 3 of the License, or
 *       (at your option) any later version.
 *
 *       This program is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU General Public License for more details.
 *
 *       You should have received a copy of the GNU General Public License
 *       along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.solostudios.qev.framework.api.actions;

import com.solostudios.qev.framework.api.Client;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;
import java.util.function.Function;


public interface Action<T> {
    /**
     * Gets the client.
     *
     * @return The client.
     */
    Client getClient();
    
    T get();
    
    T await(long millis) throws TimeoutException;
    
    T await(long timeout, TimeUnit unit);
    
    void getAsync(Consumer<T> action);
    
    void getAsync(Consumer<T> action, Consumer<? extends Exception> onException);
    
    void onException(Consumer<? extends Exception> onException);
    
    CompletableFuture<T> submit();
    
    <V> Action<V> andThen(Function<T, V> function);
    
    boolean isCompleted();
    
    boolean isCanceled();
}

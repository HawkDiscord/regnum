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

package cc.hawkbot.regnum.client.interaction;

import cc.hawkbot.regnum.client.Regnum;
import cc.hawkbot.regnum.client.util.EmbedUtil;
import cc.hawkbot.regnum.client.util.Emotes;
import cc.hawkbot.regnum.client.util.SafeMessage;
import cc.hawkbot.regnum.client.util.TranslationUtil;
import com.google.common.base.Preconditions;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

@SuppressWarnings("unused")
public class PaginatorBuilder<T> extends ReactableMessageBuilder<Paginator<T>, PaginatorBuilder<T>> {

    private TextChannel channel;
    private LinkedList<T> content = new LinkedList<>();
    private int pageSize = 10;
    private Function<T, String> formatter = Objects::toString;
    private String title;


    public PaginatorBuilder(Regnum regnum) {
        super(regnum);
    }

    /**
     * Adds an element into the content list.
     *
     * @param element the element
     * @return the current builder
     */
    public PaginatorBuilder<T> addElement(T element) {
        content.add(element);
        return this;
    }

    /**
     * Sets the new list of content.
     * <strong>warning</strong> This will clear all previous elements which
     * were added with {@link PaginatorBuilder<T>#addElement(Object)} method
     *
     * @param content the new content in a linked list {@link LinkedList}
     * @return the current builder
     */
    public PaginatorBuilder<T> setContent(LinkedList<T> content) {
        this.content = content;
        return this;
    }

    /**
     * Overload for {@link PaginatorBuilder<T>#setContent(LinkedList)} which converts a
     * list into a linked list.
     *
     * @param content the content in any type of a list {@link List}
     * @return the current builder
     * @see PaginatorBuilder<T>#setContent(LinkedList)
     */
    public PaginatorBuilder<T> setContent(List<T> content) {
        this.content = new LinkedList<>(content);
        return this;
    }

    /**
     * Sets the formatter for elements.
     * Has to be a method which converts the entities into Strings
     *
     * @param formatter the formatter
     * @return the current builder
     */
    public PaginatorBuilder<T> setFormatter(Function<T, String> formatter) {
        this.formatter = formatter;
        return this;
    }

    /**
     * Sets the title of the list.
     *
     * @param title the new title
     * @return the current builder
     */
    public PaginatorBuilder<T> setTitle(String title) {
        this.title = title;
        return this;
    }

    /**
     * Returns the current paginated content.
     * @return the current paginated content.
     */
    public LinkedList<T> getContent() {
        return content;
    }

    /**
     * Returns the current page size.
     * @return the current page size
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * Returns the current formatter.
     * @return the current formatter
     */
    public Function<T, String> getFormatter() {
        return formatter;
    }

    /**
     * Returns the current title.
     * @return the current title
     */
    public String getTitle() {
        return title;
    }

    public PaginatorBuilder<T> setPageSize(int pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public TextChannel getChannel() {
        return channel;
    }

    public PaginatorBuilder<T> setChannel(TextChannel channel) {
        this.channel = channel;
        return this;
    }

    @Override
    protected void checks() {
        Preconditions.checkState(!content.isEmpty(), "Content must not be empty");
        Preconditions.checkNotNull(title, "Title must not be null");
    }

    @Override
    public Paginator<T> build() {
        checks();
        var user = getAuthorizedUsers().get(0);
        var loadingMessage = SafeMessage.sendMessage(
                EmbedUtil.info(
                        TranslationUtil.translate(getRegnum(),
                                "phrases.list.loading.title",
                                user),
                        String.format(TranslationUtil.translate(getRegnum(),
                                "phrases.list.loading.description",
                                user),
                Emotes.LOADING)),
                channel
        ).complete();
        return new Paginator<>(
                getRegnum(),
                loadingMessage,
                getAuthorizedUsers(),
                getTimeout(),
                getTimeUnit(),
                content,
                pageSize,
                formatter,
                title
        );
    }
}

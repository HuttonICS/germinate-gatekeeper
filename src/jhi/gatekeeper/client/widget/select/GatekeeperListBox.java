/*
 *  Copyright 2017 Sebastian Raubach, Toby Philp and Paul Shaw from the
 *  Information and Computational Sciences Group at The James Hutton Institute, Dundee
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package jhi.gatekeeper.client.widget.select;

import com.google.gwt.editor.client.*;
import com.google.gwt.editor.client.adapters.*;
import com.google.gwt.event.logical.shared.*;
import com.google.gwt.event.shared.*;
import com.google.gwt.text.shared.*;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.view.client.*;

import java.util.*;

/**
 * {@link GatekeeperListBox} is a copy of {@link ValueListBox} with a couple of additional methods like: <ul> <li>{@link #selectItem(String)}</li>
 * <li>{@link #selectItem(int, boolean)}</li> <li>{@link #getSelectedIndex()}</li> <li>{@link #getElements()}</li> </ul>
 * <p/>
 * A {@link Renderer Renderer<T>} is used to get user-presentable strings to display in the select element.
 *
 * @param <T> the value type
 * @author Sebastian Raubach
 */
public class GatekeeperListBox<T> extends Composite implements Focusable, HasConstrainedValue<T>, HasEnabled, IsEditor<TakesValueEditor<T>>
{

	private final List<T>              values          = new ArrayList<>();
	private final Map<Object, Integer> valueKeyToIndex = new HashMap<>();
	private final Renderer<T>    renderer;
	private final ProvidesKey<T> keyProvider;

	private TakesValueEditor<T> editor;
	private T                   value;

	public GatekeeperListBox(Renderer<T> renderer)
	{
		this(renderer, new SimpleKeyProvider<>());
	}

	public GatekeeperListBox(Renderer<T> renderer, ProvidesKey<T> keyProvider)
	{
		this.keyProvider = keyProvider;
		this.renderer = renderer;
		initWidget(new ListBox());

		getListBox().addChangeHandler(event ->
		{
			int selectedIndex = getListBox().getSelectedIndex();

			if (selectedIndex < 0)
			{
				return; // Not sure why this happens during addValue
			}
			T newValue = values.get(selectedIndex);
			setValue(newValue, true);
		});
	}

	/**
	 * Returns the index of the selected item
	 *
	 * @return The index of the selected item or -1 if no item is selected
	 */
	public int getSelectedIndex()
	{
		return values.indexOf(getValue());
	}

	/**
	 * Tries to select the item with the given display text, i.e. the value returned by the {@link Renderer}.
	 * <p/>
	 * If no item is found, this method is a NO-OP
	 *
	 * @param item The display text of the item to select
	 * @see Renderer#render(Object)
	 */
	public void selectItem(String item)
	{
		for (T row : values)
		{
			String name = renderer.render(row);

			if (name.equalsIgnoreCase(item))
			{
				setValue(row, true);
				return;
			}
		}
	}

	/**
	 * Selects the item at the given index
	 *
	 * @param index      The index of the element to select
	 * @param fireEvents fire events if true and value is new
	 */
	public void selectItem(int index, boolean fireEvents)
	{
		setValue(getElements().get(index), fireEvents);
	}

	/**
	 * Returns an unmodifiable view of the elements
	 *
	 * @return An unmodifiable view of the elements
	 */
	public List<T> getElements()
	{
		return Collections.unmodifiableList(values);
	}

	/**
	 * Clears this {@link GatekeeperListBox}.
	 */
	public void clear()
	{
		values.clear();
		valueKeyToIndex.clear();
		ListBox listBox = getListBox();
		listBox.clear();
	}

	public HandlerRegistration addValueChangeHandler(ValueChangeHandler<T> handler)
	{
		return addHandler(handler, ValueChangeEvent.getType());
	}

	/**
	 * Returns a {@link TakesValueEditor} backed by the ValueListBox.
	 */
	public TakesValueEditor<T> asEditor()
	{
		if (editor == null)
		{
			editor = TakesValueEditor.of(this);
		}
		return editor;
	}

	@Override
	public int getTabIndex()
	{
		return getListBox().getTabIndex();
	}

	@Override
	public void setTabIndex(int index)
	{
		getListBox().setTabIndex(index);
	}

	public T getValue()
	{
		return value;
	}

	/**
	 * Set the value and display it in the select element. Add the value to the acceptable set if it is not already there.
	 */
	public void setValue(T value)
	{
		setValue(value, false);
	}

	@Override
	public boolean isEnabled()
	{
		return getListBox().isEnabled();
	}

	@Override
	public void setEnabled(boolean enabled)
	{
		getListBox().setEnabled(enabled);
	}

	public void setAcceptableValues(Collection<T> newValues)
	{
		clear();

		newValues.forEach(this::addValue);

		updateListBox();
	}

	public void setAcceptableValues(T[] newValues)
	{
		clear();

		for (T nextNewValue : newValues)
		{
			addValue(nextNewValue);
		}

		updateListBox();
	}

	@Override
	public void setAccessKey(char key)
	{
		getListBox().setAccessKey(key);
	}

	@Override
	public void setFocus(boolean focused)
	{
		getListBox().setFocus(focused);
	}

	public void reselectValue()
	{
		ValueChangeEvent.fire(this, value);
	}

	public void setValue(T value, boolean fireEvents)
	{
		if (value == this.value || (this.value != null && this.value.equals(value)))
		{
			return;
		}

		T before = this.value;
		this.value = value;
		updateListBox();

		if (fireEvents)
		{
			ValueChangeEvent.fireIfNotEqual(this, before, value);
		}
	}

	private void addValue(T value)
	{
		Object key = keyProvider.getKey(value);
		if (valueKeyToIndex.containsKey(key))
		{
			throw new IllegalArgumentException("Duplicate value: " + value);
		}

		valueKeyToIndex.put(key, values.size());
		values.add(value);
		getListBox().addItem(renderer.render(value));
		assert values.size() == getListBox().getItemCount();
	}

	private ListBox getListBox()
	{
		return (ListBox) getWidget();
	}

	private void updateListBox()
	{
		Object key = keyProvider.getKey(value);
		Integer index = valueKeyToIndex.get(key);
		if (index == null)
		{
			addValue(value);
		}

		index = valueKeyToIndex.get(key);
		getListBox().setSelectedIndex(index);
	}
}

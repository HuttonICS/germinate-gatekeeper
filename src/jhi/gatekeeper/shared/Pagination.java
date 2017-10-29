/*
 * Copyright 2017 Information and Computational Sciences,
 * The James Hutton Institute.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jhi.gatekeeper.shared;

import java.io.*;

/**
 * @author Sebastian Raubach
 */
public class Pagination implements Serializable
{
	public static final Pagination DEFAULT = new Pagination(0, Integer.MAX_VALUE);

	private int start;
	private int size;
	private Long resultSize = null;

	public Pagination()
	{
	}

	public Pagination(int start, int size)
	{
		this.start = start;
		this.size = size;
	}

	public int getStart()
	{
		return start;
	}

	public void setStart(int start)
	{
		this.start = start;
	}

	public int getSize()
	{
		return size;
	}

	public void setSize(int size)
	{
		this.size = size;
	}

	public Long getResultSize()
	{
		return resultSize;
	}

	public Pagination setResultSize(Long resultSize)
	{
		this.resultSize = resultSize;
		return this;
	}
}

/*
 * Copyright (C) 2023 Nicola De Nisco
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fileoperator;

import java.io.File;
import java.util.List;
import org.apache.commons.io.IOCase;
import org.apache.commons.io.filefilter.SuffixFileFilter;

/**
 * Filtro per esclusione.
 * Esclude i file che terminano con una estensione specificata.
 *
 * @author Nicola De Nisco
 */
public class SuffixFileFilterEscludi extends SuffixFileFilter
{
  public SuffixFileFilterEscludi(List<String> suffixes)
  {
    super(suffixes);
  }

  public SuffixFileFilterEscludi(List<String> suffixes, IOCase caseSensitivity)
  {
    super(suffixes, caseSensitivity);
  }

  public SuffixFileFilterEscludi(String suffix)
  {
    super(suffix);
  }

  public SuffixFileFilterEscludi(String... suffixes)
  {
    super(suffixes);
  }

  public SuffixFileFilterEscludi(String suffix, IOCase caseSensitivity)
  {
    super(suffix, caseSensitivity);
  }

  public SuffixFileFilterEscludi(String[] suffixes, IOCase caseSensitivity)
  {
    super(suffixes, caseSensitivity);
  }

  /**
   * Checks to see if the file name ends with the suffix.
   *
   * @param file the File to check
   * @return false if the file name ends with one of our suffixes
   */
  @Override
  public boolean accept(final File file)
  {
    return !super.accept(file);
  }

  /**
   * Checks to see if the file name ends with the suffix.
   *
   * @param file the File directory
   * @param name the file name
   * @return false if the file name ends with one of our suffixes
   */
  @Override
  public boolean accept(final File file, final String name)
  {
    return !super.accept(file, name);
  }
}

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
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import org.apache.commons.io.IOCase;
import org.apache.commons.io.filefilter.AgeFileFilter;
import org.apache.commons.io.filefilter.AndFileFilter;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.RegexFileFilter;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.commonlib5.utils.CommonFileUtils;

/**
 * Esegue scansione.
 *
 * @author Nicola De Nisco
 */
public class Runner
{
  private final List<IOFileFilter> fileFilters = new ArrayList<>();
  private FileFilter filtro;
  private File dirSposta, dirCopia;
  private int num, numCancella, numCopia, numMuovi;

  public void esegui()
     throws Exception
  {
    preparaFiltro();

    if(FileOperator.actionSposta)
    {
      dirSposta = new File(FileOperator.dirSposta);
      if(!dirSposta.isDirectory())
        throw new IllegalArgumentException("Sposta non possibile: " + dirSposta.getAbsolutePath() + " non esite o non è una directory.");
    }

    if(FileOperator.actionCopia)
    {
      dirCopia = new File(FileOperator.dirCopia);
      if(!dirCopia.isDirectory())
        throw new IllegalArgumentException("Copia non possibile: " + dirCopia.getAbsolutePath() + " non esite o non è una directory.");
    }

    for(String s : FileOperator.dirList)
    {
      File dir = new File(s);
      if(dir.isDirectory())
      {
        if(FileOperator.verbose > 0)
          System.out.println("BEGIN: scan " + dir.getAbsolutePath());

        esegui(dir);
      }
      else
        System.out.println("WARNING: la directory " + s + " non esiste o non è una directory.");
    }

    if(numCancella > 0)
      System.out.println("Cancellati " + numCancella + " files.");
    if(numCopia > 0)
      System.out.println("Copiati " + numCopia + " files.");
    if(numMuovi > 0)
      System.out.println("Spostati " + numMuovi + " files.");
  }

  private void preparaFiltro()
     throws Exception
  {
    if(FileOperator.filtro != null)
    {
      String[] estensioni = FileOperator.filtro.split(",");
      fileFilters.add(new SuffixFileFilter(estensioni, IOCase.SENSITIVE));
    }
    if(FileOperator.filtroExt != null)
    {
      fileFilters.add(new RegexFileFilter(FileOperator.filtroExt));
    }
    if(FileOperator.maxGiorni != 0)
    {
      GregorianCalendar cal = new GregorianCalendar();
      cal.add(Calendar.DAY_OF_YEAR, -FileOperator.maxGiorni);
      fileFilters.add(new AgeFileFilter(cal.getTime(), true));
    }
    if(FileOperator.filtroEscludi != null)
    {
      String[] estensioni = FileOperator.filtroEscludi.split(",");
      fileFilters.add(new SuffixFileFilterEscludi(estensioni, IOCase.SENSITIVE));
    }

    switch(fileFilters.size())
    {
      case 0:
        throw new IllegalArgumentException("Nessun filtro impostato: operazione rifiutata.");

      case 1:
        filtro = fileFilters.get(0);
        break;

      default:
        filtro = new AndFileFilter(fileFilters);
        break;
    }
  }

  private void esegui(File dir)
     throws Exception
  {
    if(FileOperator.verbose > 0)
      System.out.println("DIR: " + dir.getAbsolutePath());

    File[] files = dir.listFiles(filtro);

    if(files != null)
    {
      boolean closeVerbose = false;

      for(File f : files)
      {
        if(!f.isDirectory() && f.canWrite())
        {
          num++;

          if(FileOperator.verbose == 2)
          {
            System.out.println("FOUND: " + f.getAbsolutePath());
          }
          else if(FileOperator.verbose == 1)
          {
            System.out.print(".");
            closeVerbose = true;
          }

          if(!FileOperator.dryrun)
          {
            if(FileOperator.actionCopia)
            {
              numCopia++;
              CommonFileUtils.copyFile(f, new File(dirCopia, f.getName()));
            }

            if(FileOperator.actionSposta)
            {
              numMuovi++;
              CommonFileUtils.moveFile(f, new File(dirSposta, f.getName()));
            }
            else if(FileOperator.actionCancella)
            {
              numCancella++;
              boolean okdelete = f.delete();

              if(FileOperator.verbose > 0 && !okdelete)
                System.out.println("ERROR: non posso cancellare " + f.getAbsolutePath());
            }
          }

          // periodicamente forza una garbage collection
          if((num % 1000) == 0)
            System.gc();
        }
      }

      if(closeVerbose)
        System.out.println("!");
    }

    if(FileOperator.recurse)
    {
      File[] dirs = dir.listFiles();

      if(dirs != null)
      {
        for(File d : dirs)
        {
          if(d.isDirectory())
            esegui(d);
        }
      }
    }

    if(FileOperator.deleteDirEmpty && !FileOperator.dryrun)
    {
      File[] dirs = dir.listFiles();
      if(dirs == null || dirs.length == 0)
      {
        boolean okdelete = dir.delete();

        if(FileOperator.verbose > 0 && !okdelete)
          System.out.println("ERROR: non posso cancellare " + dir.getAbsolutePath());
      }
    }
  }
}

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

import gnu.getopt.Getopt;
import gnu.getopt.LongOpt;
import java.util.ArrayList;
import java.util.List;
import org.commonlib5.utils.LongOptExt;
import org.commonlib5.utils.StringOper;

/**
 * Pulizia files.
 *
 * @author Nicola De Nisco
 */
public class FileOperator
{
  public static final String appVersion = "1.0";
  public static final List<String> dirList = new ArrayList<>();
  public static int maxGiorni, verbose;
  public static String filtro, filtroExt, dirSposta, dirCopia;
  public static boolean recurse, dryrun, actionCancella, actionCopia, actionSposta;
  public static final Runner run = new Runner();

  public static void initialize(String[] args)
     throws Exception
  {
    LongOptExt longopts[] = new LongOptExt[]
    {
      new LongOptExt("help", LongOpt.NO_ARGUMENT, null, 'h', "visualizza questo messaggio ed esce"),
      new LongOptExt("recurse", LongOpt.NO_ARGUMENT, null, 'r', "analizza directory indicate e tutte le sottodirectory"),
      new LongOptExt("dry-run", LongOpt.NO_ARGUMENT, null, 'd', "dry-run stampa operazioni a console ma non tocca i files"),
      new LongOptExt("verbose", LongOpt.NO_ARGUMENT, null, 'v', "aumenta messagi in output"),
      new LongOptExt("older-days", LongOpt.REQUIRED_ARGUMENT, null, 'G', "eta massima dei file in giorni"),
      new LongOptExt("filtro", LongOpt.REQUIRED_ARGUMENT, null, 'f', "filtro sui files (.txt,.pip)"),
      new LongOptExt("filtro-ext", LongOpt.REQUIRED_ARGUMENT, null, 'F', "filtro esteso regular expression"),
      new LongOptExt("delete", LongOpt.NO_ARGUMENT, null, 'D', "attiva cancellazione sui target"),
      new LongOptExt("copy", LongOpt.REQUIRED_ARGUMENT, null, 'C', "copia i files nella directory indicata"),
      new LongOptExt("move", LongOpt.REQUIRED_ARGUMENT, null, 'M', "sposta i files nella directory indicata"),
    };

    String optString = LongOptExt.getOptstring(longopts);
    Getopt g = new Getopt("FileOperator", args, optString, longopts);
    g.setOpterr(false); // We'll do our own error handling

    int c;
    while((c = g.getopt()) != -1)
    {
      switch(c)
      {
        case '?':
        case 'h':
          help(longopts);
          return;

        case 'G':
          maxGiorni = StringOper.parse(g.getOptarg(), maxGiorni);
          break;

        case 'r':
          recurse = true;
          break;
        case 'd':
          dryrun = true;
          break;
        case 'v':
          verbose++;
          break;

        case 'f':
          filtro = g.getOptarg();
          break;

        case 'F':
          filtroExt = g.getOptarg();
          break;

        case 'D':
          actionCancella = true;
          break;
        case 'C':
          actionCopia = true;
          dirCopia = g.getOptarg();
          break;
        case 'M':
          actionSposta = true;
          dirSposta = g.getOptarg();
          break;

        default:
          System.out.println("Opzione '" + ((char) c) + "' ignorata.");
      }
    }

    for(int i = g.getOptind(); i < args.length; i++)
    {
      dirList.add(args[i]);
    }
  }

  public static void help(LongOptExt longopts[])
  {
    System.out.printf(
       "FileOperator - ver. %s\n"
       + "Utility per la cancellazione selettiva di files.\n"
       + "modo d'uso:\n"
       + "  fileOperator [-h] dirtodel1 dirtodel2 ...\n", appVersion);

    for(LongOptExt l : longopts)
    {
      System.out.println(l.getHelpMsg());
    }

    System.out.println();
    System.exit(0);
  }

  /**
   * Avvia il programma.
   * @param args the command line arguments
   */
  public static void main(String[] args)
  {
    try
    {
      initialize(args);
      run.esegui();
      System.exit(0);
    }
    catch(IllegalArgumentException e)
    {
      System.err.println("FATAL: " + e.getMessage());
    }
    catch(Exception e)
    {
      e.printStackTrace();
      System.exit(-1);
    }
  }
}

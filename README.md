# FileOperator

## Simple utility for delete, copy, move massive files.

```
FileOperator - ver. 1.0
Utility per la cancellazione selettiva di files.
modo d'uso:
  fileOperator [-h] dirtodel1 dirtodel2 ...
  -h --help                      visualizza questo messaggio ed esce
  -r --recurse                   analizza directory indicate e tutte le sottodirectory
  -d --dry-run                   dry-run stampa operazioni a console ma non tocca i files
  -v --verbose                   aumenta messagi in output
  -G --older-days <val>          eta massima dei file in giorni
  -f --filtro <val>              filtro sui files (.txt,.pip)
  -F --filtro-ext <val>          filtro esteso regular expression
  -D --delete                    attiva cancellazione sui target
  -C --copy <val>                copia i files nella directory indicata
  -M --move <val>                sposta i files nella directory indicata
```

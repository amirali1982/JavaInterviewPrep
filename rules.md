# AGENT SYSTEM CONTEXT
The user is working on **Windows** inside a **Google Drive Synced Folder**.
The terminal shell is **Command Prompt (cmd.exe)**.

## CRITICAL DRIVE BEHAVIOR
You are running on a Cloud Sync Virtual Drive. File operations are SLOW and prone to LOCKS.

1. **The "Wait" Protocol**:
   - If you modify a file, **WAIT 2 SECONDS** before running it.
   - If you get "The process cannot access the file because it is being used by another process", **WAIT 2 SECONDS and RETRY**. Do not give up immediately.

2. **File Deletion**:
   - Google Drive often locks files preventing deletion.
   - Use: `del /F /Q <filename>`
   - If that fails, ignore it and move on. Do NOT try to fix the file system permissions.

## WINDOWS COMMAND OVERRIDES
| Action       | LINUX (Forbidden) | WINDOWS CMD (Required)       |
| :---         | :---              | :---                         |
| **Delete** | `rm`              | `del /F /Q`                  |
| **Read** | `cat`             | `type`                       |
| **List** | `ls`              | `dir`                        |
| **Search** | `grep`            | `findstr`                    |

## IGNORED FILES
- Do not touch files starting with `.~` or ending in `.tmp`. These are temporary Google Drive sync artifacts.
#!/bin/zsh

# ═════════════════════════════════════════════════════════════
#  push.sh — Auto push your code to GitHub
#
#  HOW TO USE:
#    1. Open a terminal in this project folder
#    2. Run:  ./push.sh
#    3. Type your commit message when prompted, then press Enter
#
#  HOW TO MAKE IT EXECUTABLE (run this once, you never need it again):
#    chmod +x push.sh
#
#  WHAT THIS SCRIPT DOES, STEP BY STEP:
#    1. Asks you to type a commit message
#    2. Checks that you didn't leave it empty
#    3. git add .        → stages ALL changed files
#    4. git commit -m    → creates a commit with your message
#    5. git push         → uploads your commits to GitHub
# ═════════════════════════════════════════════════════════════

# ─────────────────────────────────────────────────────────────
#  ANSI color codes — purely cosmetic, makes output easier to read
#  \033[0m  = reset (back to normal color)
#  \033[32m = green
#  \033[31m = red
#  \033[33m = yellow
#  \033[36m = cyan
# ─────────────────────────────────────────────────────────────
RESET="\033[0m"
GREEN="\033[32m"
RED="\033[31m"
YELLOW="\033[33m"
CYAN="\033[36m"

echo ""
echo "${CYAN}════════════════════════════════════${RESET}"
echo "${CYAN}   🚀  GitHub Auto Push Script       ${RESET}"
echo "${CYAN}════════════════════════════════════${RESET}"
echo ""

# ─────────────────────────────────────────────────────────────
#  STEP 1 — Ask for a commit message
#
#  "read" is a shell command that waits for the user to type
#  something and press Enter. It stores what was typed in the
#  variable named after -r (raw mode, preserves backslashes).
#  -p "..." is the prompt text shown to the user.
# ─────────────────────────────────────────────────────────────
echo "${YELLOW}📝  Enter your commit message:${RESET}"
read -r COMMIT_MSG

# ─────────────────────────────────────────────────────────────
#  STEP 2 — Validate that the message is not empty
#
#  -z "$COMMIT_MSG" is true when the variable is empty or
#  contains only whitespace.
#  If empty, we print an error and exit with code 1
#  (non-zero exit = something went wrong).
# ─────────────────────────────────────────────────────────────
if [[ -z "${COMMIT_MSG// /}" ]]; then
  echo ""
  echo "${RED}❌  Commit message cannot be empty. Aborting.${RESET}"
  echo ""
  exit 1
fi

# ─────────────────────────────────────────────────────────────
#  STEP 3 — Show a summary of what will be pushed
#
#  git status --short gives a compact view of changed files.
#  We show this so you can review before committing.
# ─────────────────────────────────────────────────────────────
echo ""
echo "${CYAN}📂  Files that will be staged:${RESET}"
git status --short

echo ""

# ─────────────────────────────────────────────────────────────
#  STEP 4 — Confirm before pushing
#
#  We give one last chance to cancel.
#  read -r -n 1 reads a single character (no need to press Enter).
#  We match against Y or y using a case statement.
# ─────────────────────────────────────────────────────────────

# ─────────────────────────────────────────────────────────────
#  STEP 5 — git add .
#
#  Stages ALL modified, new, and deleted files in the project.
#  The dot (.) means "everything in the current directory
#  and all subdirectories."
#
#  If this command fails (non-zero exit code) the || block runs,
#  prints an error, and exits the script.
# ─────────────────────────────────────────────────────────────
echo ""
echo "${CYAN}📦  Staging all changes...${RESET}"
git add . || { echo "${RED}❌  git add failed. Aborting.${RESET}"; exit 1; }

# ─────────────────────────────────────────────────────────────
#  STEP 6 — git commit
#
#  Creates a snapshot (commit) with the message you typed.
#  -m means "use the next string as the commit message."
# ─────────────────────────────────────────────────────────────
echo "${CYAN}✍️   Committing...${RESET}"
git commit -m "$COMMIT_MSG" || { echo "${RED}❌  git commit failed. Aborting.${RESET}"; exit 1; }

# ─────────────────────────────────────────────────────────────
#  STEP 7 — git push
#
#  Uploads your commits to GitHub (the "origin" remote, on the
#  current branch you are on).
# ─────────────────────────────────────────────────────────────
echo "${CYAN}🚀  Pushing to GitHub...${RESET}"
git push || { echo "${RED}❌  git push failed. Check your network or GitHub credentials.${RESET}"; exit 1; }

# ─────────────────────────────────────────────────────────────
#  DONE
# ─────────────────────────────────────────────────────────────
echo ""
echo "${GREEN}✅  Successfully pushed: \"${COMMIT_MSG}\"${RESET}"
echo ""

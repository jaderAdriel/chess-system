# ♟️ Java Chess Terminal Game

A complete chess implementation in Java with terminal interface, following OOP principles and chess rules.

![Chess Terminal Demo](https://github.com/jaderAdriel/chess-system/blob/master/docs/demo.gif)

## 🚀 Features

| Category        | Features                                                                 |
|-----------------|--------------------------------------------------------------------------|
| **Game Rules**  | Check/checkmate detection • Castling • En passant • Pawn promotion       |
| **UX**          | Color-coded board • Move highlighting • Algebraic notation input         |
| **Tech**        | Polymorphic piece logic • Clean architecture • Input validation         |

## 🏗️ Project Structure

```text
src/
├── application/        # Main game flow
├── boardgame/          # Generic board components
└── chess/              # Chess-specific logic
```

## 🛠️ Installation & Usage

0. **Clone repository**
    ```bash
    git clone https://github.com/jaderAdriel/chess-system.git
    ```

1. **Compile and Start the Game**:
   ```bash
   javac -d bin src/application/Program.java
   java -cp bin application.Program
   ```

2. **Moving Pieces**:
   - When prompted:
     - First, type the **source position** (e.g., `e2`)
     - Then, type the **target position** (e.g., `e4`)
   - The game will highlight possible moves after you select a piece

3. **Special Moves**:
   - When a pawn reaches the opposite side of the board:
     - Choose one of: `Q` (Queen), `R` (Rook), `B` (Bishop), or `N` (Knight)

4. **Game Flow**:
   - Players take turns
   - Invalid moves will display an error message
   - The game ends when checkmate occurs

---

## 💡 Code Highlights

- Clear separation between generic board logic and chess-specific rules
- Exception handling for invalid inputs
- Polymorphic move validation
- Terminal interface with:
  - Colored pieces and board
  - Highlighting of valid moves
  - Display of captured pieces
  - Clear status messages

---

## 🎮 Gameplay Example

### 🔲 Selecting a piece  
![Selecting a piece](https://github.com/jaderAdriel/chess-system/blob/master/docs/select-piece.png)

### 🟡 Showing possible moves  
![Showing possible moves](https://github.com/jaderAdriel/chess-system/blob/master/docs/possible-moves.png)


## 🤝 Contributing

1. Fork the project
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request


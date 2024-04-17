# ADR 1. Design assumptions

## OBJECTIVE
Clarify the assumptions taken (which are quite a lot).

## ASSUMPTIONS
1. Mowers are not very autonomous, therefore the control system is critical.
2. The control system should validate:
    - The input file is valid.
      - The grid size is valid.
      - Every mower contains a valid initial position inside the grid.
      - Every mower contains a valid movement command.
      - There is only a single mower per coordinate.
      - The orientations are valid N, S, W, E.
    - The control system will move the mowers across the Plateau. 
      - If a mower is about to leave the grid in the next movement, the system will stop the mower's execution and the mower will remain as is.
      - If a mower is about to move to a position where there is already a mower, the system will prevent mower crashes and skip that movement. The execution will continue with a skipped step.
3. The input type is a `FILE`.
    - The first line defines the grid.
    - Subsequent lines will define the mowers and their movement instructions.
    - The file is provided as multipart data to the API endpoint.
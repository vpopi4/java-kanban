# API of application

We have standard Error Body:
- `String errorName`
- `String message`

## `/tasks`

### `GET /tasks/{id}`
Returns a stored simple task.
- Request:
  - Parameters:
    - `Integer id` not null
- Response 200:
  - Body: 
    - `Task task`
- Response 400
- Response 404:
  - if task with such `id` not found
  - Error Body

### `GET /tasks`
Returns all stored simple tasks.
- Response 200:
  - Body:
    - `Task[] tasks`

### `POST /tasks`
If `task.id` in request body not specified 
    it creates a simple task
Otherwise
    it updates a stored simple task.
- Request:
  - Body:
    - `task`:
      - `Integer id`
      - `String name` not empty and one line
      - `String description` not empty and one line
      - `"NEW" | "IN_PROGRESS" | "DONE" status`:
        - if `id` is `null` then `status` will be `"NEW"`
      - `Long duration` in milliseconds
      - `String startTime` in `yyyy-MM-ddThh:mm:ss` format
- Response 200:
  - Body:
    - `Task task` created or updated task
- Response 400
- Response 404:
  - if task with such `id` not found
  - Error Body
- Response 406:
  - if task intersect with any other task or subtask on timeline
  - Error Body

### `DELETE /tasks/{id}`
Removes a task.
- Request:
    - Parameters:
        - `Integer id` not null
- Response 201
- Response 400

## `/epics`

### `GET /epics`
Returns all stored epics.
- Response 200:
    - Body:
        - `Epic[] epics`

### `GET /epics/{id}`
Returns a stored epic.
- Request:
    - Parameters:
        - `Integer id` not null
- Response 200:
    - Body:
        - `Epic epic`
- Response 400
- Response 404:
    - if epic with such `id` not found
    - Error Body

### `GET /epics/{id}/subtasks`
Returns all subtasks of epic with specified `id`.
- Request:
    - Parameters:
        - `Integer id` not null
- Response 200:
  - Body:
    - `Subtasks[] subtasksOfEpic`
- Response 400
- Response 404:
  - if epic with such `id` not found

### `POST /epics`
Creates a epic.
- Request:
    - Body:
        - `epic`:
            - `String name` not empty and one line
            - `String description` not empty and one line
- Response 200:
    - Body:
        - `Epic epic` epic

### `DELETE /epics/{id}`
Removes an epic and associated subtasks.
- Request:
    - Parameters:
        - `Integer id` not null
- Response 201
- Response 400
 
## `/subtasks`

### `GET /subtasks/{id}`
Returns a stored subtask.
- Request:
    - Parameters:
        - `Integer id` not null
- Response 200:
    - Body:
        - `Subtask subtask`
- Response 400
- Response 404:
    - if subtask with such `id` not found
    - Error Body

### `GET /subtasks`
Returns all stored subtasks.
- Response 200:
    - Body:
        - `Subtasks[] subtasks`

### `POST /subtasks`
If `subtasks.id` in request body not specified
it creates a subtask of epic
Otherwise
it updates a subtask of epic.
Status, duration and start time of epic can be changed.
- Request:
    - Body:
        - `subtasks`:
            - `Integer id`
            - `Integer epicId` not null
            - `String name` not empty and one line
            - `String description` not empty and one line
            - `"NEW" | "IN_PROGRESS" | "DONE" status`:
                - if `id` is `null` then `status` will be `"NEW"`
            - `Long duration` in milliseconds
            - `String startTime` in `yyyy-MM-ddThh:mm:ss` format
- Response 200:
    - Body:
        - `Subtask subtask` created or updated subtask
- Response 400
- Response 404:
    - if epic with such `epicId` not found or
    - if subtask with such `id` not found
    - Error Body
- Response 406:
    - if task intersect with any other task or subtask on timeline
    - Error Body

### `DELETE /subtasks/{id}`
Removes a subtask.
Status, duration and start time of epic can be changed.
- Request:
    - Parameters:
        - `Integer id` not null
- Response 201
- Response 400

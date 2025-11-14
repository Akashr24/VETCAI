VETCAI Frontend

This is a tiny static frontend to exercise the local VETCAI API.

Files:
- index.html — simple UI to call endpoints
- app.js — JavaScript that performs fetch requests to the API
- postman.json — copy of the Postman collection for import

How to use
1. Ensure the backend is running (default: http://localhost:8080).
2. Open `vetcai-frontend/index.html` in your browser (double-click or serve it with a static server).
3. Use the controls to call endpoints. Responses appear in the "Response" box.

Notes
- If the browser blocks requests due to CORS, run the frontend via a local static server (e.g., `npx serve` or `python -m http.server`) or enable CORS on the backend.
- The `postman.json` file is included for quick import into Postman.
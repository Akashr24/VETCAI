(function(){
  const qs = id => document.getElementById(id);
  const msg = qs('message');

  function showStatus(text, ok){
    msg.textContent = text;
    msg.style.color = ok ? 'var(--success)' : 'var(--muted)';
  }

  async function postLogin(){
    const base = qs('baseUrl').value.replace(/\/$/, '');
    const username = qs('username').value.trim();
    const password = qs('password').value;
    if(!username || !password){ showStatus('Please fill username and password', false); return; }

    // Local dev shortcut: accept hard-coded admin credentials and redirect locally
    if (username === 'Akash' && password === 'akash@0987') {
      // store a lightweight token for local dev and redirect to main UI
      localStorage.setItem('vetcai_token', 'local-dev-akash-token');
      localStorage.setItem('vetcai_base', base);
      showStatus('Login successful — redirecting to main UI...', true);
      setTimeout(() => { window.location.href = 'index.html'; }, 600);
      return;
    }

    try{
      const res = await fetch(base + '/api/admin/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ username, password })
      });

      const txt = await res.text();
      let body = null;
      try{ body = JSON.parse(txt); } catch(e){ body = txt; }

      if(res.ok){
        // Accept both { success: true, token: '...' } and raw user object
        if(body && typeof body === 'object'){
          if(body.token){
            localStorage.setItem('vetcai_token', body.token);
            localStorage.setItem('vetcai_base', base);
            showStatus('Login successful — token saved to localStorage', true);
          } else if(body.success === true){
            showStatus('Login successful', true);
          } else {
            showStatus('Login OK — response received', true);
          }
        } else {
          showStatus('Login OK — response: ' + String(body), true);
        }
      } else {
        const msgText = (body && body.message) ? body.message : (typeof body === 'string' ? body : 'Login failed');
        showStatus('Error: ' + msgText, false);
      }
    }catch(err){
      showStatus('Network error: ' + (err.message || err), false);
    }
  }

  qs('btnLogin').addEventListener('click', postLogin);
  qs('btnClear').addEventListener('click', ()=>{
    qs('username').value = '';
    qs('password').value = '';
    showStatus('', false);
  });

  // prefill base url from previous session if present
  const prev = localStorage.getItem('vetcai_base');
  if(prev) qs('baseUrl').value = prev;
})();
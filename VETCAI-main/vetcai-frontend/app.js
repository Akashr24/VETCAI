(function(){
  function qs(id){ return document.getElementById(id); }
  const out = qs('output');
  function show(x){ out.textContent = typeof x === 'string' ? x : JSON.stringify(x, null, 2); }
  function base(){ return qs('baseUrl').value.replace(/\/$/, ''); }

  async function request(method, path, body){
    try{
      const url = base() + path;
      const opts = { method, headers: {} };
      if (body) { opts.headers['Content-Type'] = 'application/json'; opts.body = JSON.stringify(body); }
      const resp = await fetch(url, opts);
      const text = await resp.text();
      try { const json = JSON.parse(text); show({ status: resp.status, body: json }); }
      catch(e){ show({ status: resp.status, body: text }); }
    }catch(e){ show({ error: e && e.message ? e.message : String(e) }); }
  }

  qs('btnSummary').addEventListener('click', ()=> request('GET','/api/dashboard/district-summary'));
  qs('btnGetDistricts').addEventListener('click', ()=> request('GET','/api/districts'));
  qs('btnCreateDistrict').addEventListener('click', ()=> {
    const d = { districtName: qs('districtName').value, providerName: qs('providerName').value, regionCode: qs('regionCode').value };
    request('POST','/api/districts', d);
  });

  qs('btnLedOn').addEventListener('click', ()=> {
    const color = qs('ledColor').value;
    request('POST',`/api/led/${color}/on`);
  });
  qs('btnLedOff').addEventListener('click', ()=> {
    const color = qs('ledColor').value;
    request('POST',`/api/led/${color}/off`);
  });

  qs('btnGetTariff').addEventListener('click', ()=>{
    const id = parseInt(qs('tariffDistrictId').value, 10);
    if (!id) { show({ error: 'Enter District ID' }); return; }
    request('GET', `/api/tariffs/${id}`);
  });
  qs('btnSaveTariff').addEventListener('click', ()=>{
    const id = parseInt(qs('tariffDistrictId').value, 10);
    if (!id) { show({ error: 'Enter District ID' }); return; }
    const body = {
      lowLimit: parseFloat(qs('lowLimit').value),
      mediumLimit: parseFloat(qs('mediumLimit').value),
      highLimit: parseFloat(qs('highLimit').value)
    };
    request('PUT', `/api/tariffs/${id}`, body);
  });

  qs('btnGetUsage').addEventListener('click', ()=>{
    const id = parseInt(qs('usageDistrictId').value, 10);
    if (!id) { show({ error: 'Enter District ID' }); return; }
    request('GET', `/api/usage/${id}`);
  });

  // quick helper to set sample values
  qs('districtName').value = 'Central District';
  qs('providerName').value = 'Acme Energy';
  qs('regionCode').value = 'CEN-01';
})();
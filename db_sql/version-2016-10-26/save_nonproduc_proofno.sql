--存储各实例编号对应的凭证编号的表sql
create table save_nonproduc_proofno(
      process_id varchar2(30) not null,
      proof_no varchar2(30) not null,
      constraint  process_id_ul unique (process_id)
     )
     